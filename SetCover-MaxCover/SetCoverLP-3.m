ns = [100 200 500 1000 2000];
ms = ns/5;

q_max = length(ns);
t_max = 10;

XL = zeros(t_max,q_max);
XL_t = zeros(t_max,q_max);
XID = zeros(t_max,q_max);
XID_t = zeros(t_max,q_max);
XIR = zeros(t_max,q_max);
XIR_t = zeros(t_max,q_max);
iters = zeros(t_max,q_max);

for q=1:q_max
    n = ns(q);
    m = ms(q);
    for t=1:t_max
        q
        t
        S = zeros(n,m);

        for i=1:m
            k = randi(n);
            r = randsample(n,k);
            S(r,i) = 1;
        end

        f = ones(m,1);
        A = -S;
        b = -ones(n,1);
        Aeq = [];
        beq = [];
        lb = zeros(m,1);
        ub = ones(m,1);
        
        tic
        xl = linprog(f,A,b,Aeq,beq,lb,ub);
        xl_sol = sum(xl);
        XL(t,q) = xl_sol;
        XL_t(t,q) = toc;
        
        tic
        d = max(sum(S,2)); % deterministic rounding: book section 1.3
        xid = zeros(m,1);  % guaranteed feasible, d approximation
        xid(xl>=1/d) = 1;
        xid_sol = sum(xid);
        XID(t,q) = xid_sol;
        XID_t(t,q) = toc;
        
        tic 
        iter = 0;
        while true
            ra = rand(m,1);   % random rounding; book section 1.7
            xir = zeros(m,1); % likely not feasible, expected value equal to lp solution
            xir(ra<=xl) = 1;
            num_feas = sum(A*xir<=b);
            if num_feas == n % then feasible
                iters(t,q) = iter;
                break;       % else try again
            end
            iter = iter + 1;
            if iter == 100
                break
            end
        end
        xir_sol = sum(xir);
        XIR(t,q) = xir_sol;
        XIR_t(t,q) = toc;
    end
end

